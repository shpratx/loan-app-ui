import express from 'express';
import cors from 'cors';
import { randomUUID } from 'crypto';

const app = express();
app.use(cors());
app.use(express.json());

// ═══════════════════════════════════════════════════════════
// IN-MEMORY DATABASE
// ═══════════════════════════════════════════════════════════
const db = {
  products: [
    { id: '11111111-1111-1111-1111-111111111111', name: 'Cash Finance', type: 'Cash', minAmount: 5000, maxAmount: 250000, minTenure: 3, maxTenure: 60, baseRate: 20.0, earlyClosureFeePercent: 1.0, adminFeePercent: 1.5, description: 'Flexible personal finance with risk-based pricing' },
    { id: '22222222-2222-2222-2222-222222222222', name: 'Combo Finance', type: 'Combo', minAmount: 10000, maxAmount: 500000, minTenure: 6, maxTenure: 60, baseRate: 18.0, earlyClosureFeePercent: 1.5, adminFeePercent: 1.0, description: 'Combined finance package with competitive rates' },
  ],
  applications: [],
  offers: [],
  loans: [],
  payments: [],
  cards: [],
  auditLogs: [],
};

// ═══════════════════════════════════════════════════════════
// MOCK EXTERNAL SERVICES
// ═══════════════════════════════════════════════════════════
const simah = { getCreditScore: (income) => income > 8000 ? 700 : 450 };
const citc = { verify: (employer) => !employer.toUpperCase().includes('FAIL') };
const geo = { verify: () => true };
const paymentProcessor = { tokenize: (last4) => ({ token: `tok_${randomUUID().slice(0,8)}`, last4 }) };

function audit(action, resource, resourceId, userId = 'system', reason = null) {
  db.auditLogs.push({ id: randomUUID(), userId, action, resource, resourceId, reason, timestamp: new Date().toISOString() });
}

// ═══════════════════════════════════════════════════════════
// AUTH (mock)
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/auth/token', (req, res) => {
  const { username, password } = req.body;
  if (!username || !password) return res.status(400).json({ type: 'validation', title: 'Username and password required', status: 400 });
  res.json({ data: { token: `mock-jwt-${randomUUID().slice(0,8)}`, userId: randomUUID(), name: username.split('@')[0], expiresIn: 900 } });
});

// ═══════════════════════════════════════════════════════════
// HEALTH
// ═══════════════════════════════════════════════════════════
app.get('/health', (_, res) => res.json({ status: 'Healthy' }));
app.get('/health/ready', (_, res) => res.json({ status: 'Healthy', checks: { database: 'Healthy', cache: 'Healthy' } }));

// ═══════════════════════════════════════════════════════════
// PRODUCTS
// ═══════════════════════════════════════════════════════════
app.get('/api/v1/products', (_, res) => res.json({ data: db.products }));
app.get('/api/v1/products/:id', (req, res) => {
  const p = db.products.find(x => x.id === req.params.id);
  p ? res.json({ data: p }) : res.status(404).json({ type: 'not-found', title: 'Product not found', status: 404 });
});

// ═══════════════════════════════════════════════════════════
// APPLICATIONS
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/applications', (req, res) => {
  const { productId, requestedAmount, requestedTenure, fullName, nationalId, employer, monthlySalary, dateOfBirth, address, city, region, employmentType, employmentStartDate, grossIncome, otherIncome } = req.body;
  const product = db.products.find(x => x.id === productId);
  if (!product) return res.status(400).json({ type: 'validation', title: 'Invalid product', status: 400 });
  const app = { id: randomUUID(), userId: randomUUID(), productId, productName: product.name, status: 'Draft', requestedAmount, approvedAmount: null, tenure: requestedTenure, income: monthlySalary, grossIncome: grossIncome || monthlySalary, otherIncome: otherIncome || 0, employerName: employer, employmentType: employmentType || 'Government', employmentStartDate: employmentStartDate || null, fullName, nationalId, dateOfBirth: dateOfBirth || null, address: address || null, city: city || null, region: region || null, isListed: true, salaryDate: null, createdAt: new Date().toISOString() };
  db.applications.push(app);
  audit('APPLICATION_CREATED', 'Application', app.id);
  res.status(201).json({ data: app });
});

app.get('/api/v1/applications/:id', (req, res) => {
  const app = db.applications.find(x => x.id === req.params.id);
  app ? res.json({ data: app }) : res.status(404).json({ type: 'not-found', title: 'Application not found', status: 404 });
});

app.post('/api/v1/applications/:id/submit', (req, res) => {
  const app = db.applications.find(x => x.id === req.params.id);
  if (!app) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  if (app.status !== 'Draft') return res.status(422).json({ type: 'business-rule', title: 'Application must be Draft to submit', status: 422 });
  app.status = 'Submitted';
  audit('STATUS_CHANGE', 'Application', app.id, app.userId, 'Submitted');
  res.json({ data: app });
});

// ═══════════════════════════════════════════════════════════
// DECISION ENGINE (ASSESSMENT)
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/applications/:id/assess', (req, res) => {
  const app = db.applications.find(x => x.id === req.params.id);
  if (!app) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  if (app.status !== 'Submitted') return res.status(422).json({ type: 'business-rule', title: 'Must be Submitted to assess', status: 422 });

  app.status = 'Verifying';
  const product = db.products.find(x => x.id === app.productId);

  // CITC check
  if (!citc.verify(app.employerName)) {
    app.status = 'Referred'; audit('ASSESSMENT_FAILED', 'Application', app.id, 'system', 'CITC failed');
    return res.json({ data: { applicationId: app.id, status: 'Referred', approvedAmount: null, reason: 'Employment verification failed' } });
  }
  // SIMAH check
  const score = simah.getCreditScore(app.income);
  if (score < 500) {
    app.status = 'Referred'; audit('ASSESSMENT_FAILED', 'Application', app.id, 'system', `Score ${score}`);
    return res.json({ data: { applicationId: app.id, status: 'Referred', approvedAmount: null, reason: `Credit score ${score} below threshold` } });
  }
  // DBR check (33% of income)
  const maxMonthlyPayment = app.income * 0.33;
  const monthlyRate = product.baseRate / 12 / 100;
  const n = app.tenure;
  const maxLoan = maxMonthlyPayment * ((1 - Math.pow(1 + monthlyRate, -n)) / monthlyRate);
  let approved = Math.min(app.requestedAmount, maxLoan, product.maxAmount);
  approved = Math.max(approved, 0);

  if (approved < product.minAmount) {
    app.status = 'Referred'; audit('ASSESSMENT_FAILED', 'Application', app.id, 'system', 'Below minimum');
    return res.json({ data: { applicationId: app.id, status: 'Referred', approvedAmount: null, reason: `Amount below minimum ${product.minAmount}` } });
  }

  app.approvedAmount = Math.round(approved * 100) / 100;
  app.status = 'Approved';
  audit('ASSESSMENT_PASSED', 'Application', app.id, 'system', `Approved ${app.approvedAmount}`);
  res.json({ data: { applicationId: app.id, status: 'Approved', approvedAmount: app.approvedAmount, reason: null } });
});

// ═══════════════════════════════════════════════════════════
// OFFERS
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/applications/:id/offer/generate', (req, res) => {
  const app = db.applications.find(x => x.id === req.params.id);
  if (!app || app.status !== 'Approved') return res.status(422).json({ type: 'business-rule', title: 'Must be Approved', status: 422 });
  const product = db.products.find(x => x.id === app.productId);
  const monthlyRate = product.baseRate / 12 / 100;
  const monthly = app.approvedAmount * monthlyRate / (1 - Math.pow(1 + monthlyRate, -app.tenure));
  const offer = { id: randomUUID(), applicationId: app.id, amount: app.approvedAmount, tenure: app.tenure, profitRate: product.baseRate, adminFee: app.approvedAmount * product.adminFeePercent / 100, monthlyPayment: Math.round(monthly * 100) / 100, totalAmount: Math.round(monthly * app.tenure * 100) / 100, validUntil: new Date(Date.now() + 30*86400000).toISOString() };
  db.offers.push(offer);
  app.status = 'OfferGenerated';
  audit('OFFER_GENERATED', 'Offer', offer.id);
  res.status(201).json({ data: offer });
});

app.get('/api/v1/applications/:id/offer', (req, res) => {
  const offer = db.offers.find(x => x.applicationId === req.params.id);
  offer ? res.json({ data: offer }) : res.status(404).json({ type: 'not-found', title: 'No offer', status: 404 });
});

app.post('/api/v1/applications/:id/offer/accept', (req, res) => {
  const offer = db.offers.find(x => x.applicationId === req.params.id);
  if (!offer) return res.status(404).json({ type: 'not-found', title: 'No offer', status: 404 });
  const app = db.applications.find(x => x.id === req.params.id);
  app.status = 'OfferAccepted';
  // Create loan
  const loan = { id: randomUUID(), applicationId: app.id, offerId: offer.id, disbursedAmount: offer.amount - offer.adminFee, outstandingPrincipal: offer.amount, monthlyPayment: offer.monthlyPayment, tenure: offer.tenure, status: 'Active', disbursedAt: new Date().toISOString(), nextPaymentDate: new Date(Date.now() + 30*86400000).toISOString() };
  db.loans.push(loan);
  app.status = 'Active';
  audit('OFFER_ACCEPTED', 'Loan', loan.id);
  res.json({ data: { offer, loan } });
});

// ═══════════════════════════════════════════════════════════
// CARDS
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/applications/:id/card', (req, res) => {
  const { cardNumber, expiryMonth, expiryYear, autoDebitDay } = req.body;
  const last4 = cardNumber.slice(-4);
  const { token } = paymentProcessor.tokenize(last4);
  const card = { id: randomUUID(), applicationId: req.params.id, tokenReference: token, last4Digits: last4, salaryDate: autoDebitDay, isActive: true };
  db.cards.push(card);
  audit('CARD_REGISTERED', 'DebitCard', card.id);
  res.status(201).json({ data: { id: card.id, last4Digits: last4, autoDebitDay, message: 'Card registered. Single-date auto-debit configured.' } });
});

// ═══════════════════════════════════════════════════════════
// LOANS
// ═══════════════════════════════════════════════════════════
app.get('/api/v1/loans', (_, res) => res.json({ data: db.loans }));
app.get('/api/v1/loans/:id', (req, res) => {
  const loan = db.loans.find(x => x.id === req.params.id);
  loan ? res.json({ data: loan }) : res.status(404).json({ type: 'not-found', title: 'Loan not found', status: 404 });
});

app.get('/api/v1/loans/:id/settlement-figure', (req, res) => {
  const loan = db.loans.find(x => x.id === req.params.id);
  if (!loan) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  const app = db.applications.find(x => x.id === loan.applicationId);
  const product = db.products.find(x => x.id === app.productId);
  const accruedInterest = loan.outstandingPrincipal * (product.baseRate / 100 / 12);
  const earlyClosureFee = loan.outstandingPrincipal * (product.earlyClosureFeePercent / 100);
  const settlement = Math.round((loan.outstandingPrincipal + accruedInterest + earlyClosureFee) * 100) / 100;
  res.json({ data: { loanId: loan.id, outstandingPrincipal: loan.outstandingPrincipal, accruedInterest: Math.round(accruedInterest*100)/100, earlyClosureFee: Math.round(earlyClosureFee*100)/100, settlementAmount: settlement, validUntil: new Date(Date.now() + 28*86400000).toISOString() } });
});

app.get('/api/v1/loans/:id/liability-letter', (req, res) => {
  const loan = db.loans.find(x => x.id === req.params.id);
  if (!loan) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  const app = db.applications.find(x => x.id === loan.applicationId);
  const product = db.products.find(x => x.id === app.productId);
  res.json({ data: { loanId: loan.id, customerName: app.fullName, loanReference: `TF-${loan.id.slice(0,8).toUpperCase()}`, outstandingBalance: loan.outstandingPrincipal, originalAmount: loan.disbursedAmount, interestRate: `${product.baseRate}% p.a.`, remainingTenure: `${loan.tenure} months`, monthlyPayment: loan.monthlyPayment, nextPaymentDate: loan.nextPaymentDate, earlySettlementFee: `SAR ${(loan.outstandingPrincipal * product.earlyClosureFeePercent / 100).toFixed(2)}`, generatedAt: new Date().toISOString() } });
});

app.get('/api/v1/loans/:id/topup-eligibility', (req, res) => {
  const loan = db.loans.find(x => x.id === req.params.id);
  if (!loan) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  const app = db.applications.find(x => x.id === loan.applicationId);
  const currentDBR = (loan.monthlyPayment / app.income) * 100;
  const eligible = currentDBR < 25; // room for top-up within 33% cap
  res.json({ data: { loanId: loan.id, eligible, currentDBR: Math.round(currentDBR*10)/10, maxDBR: 33, reason: eligible ? null : 'DBR too high for additional lending' } });
});

// ═══════════════════════════════════════════════════════════
// PAYMENTS
// ═══════════════════════════════════════════════════════════
app.get('/api/v1/loans/:id/payments', (req, res) => {
  const payments = db.payments.filter(x => x.loanId === req.params.id);
  res.json({ data: payments });
});

app.post('/api/v1/loans/:id/payments', (req, res) => {
  const loan = db.loans.find(x => x.id === req.params.id);
  if (!loan) return res.status(404).json({ type: 'not-found', title: 'Not found', status: 404 });
  const { amount, type = 'Manual' } = req.body;
  const payment = { id: randomUUID(), loanId: loan.id, amount, type, status: 'Settled', attemptNumber: 1, processedAt: new Date().toISOString() };
  db.payments.push(payment);
  loan.outstandingPrincipal = Math.max(0, loan.outstandingPrincipal - amount);
  if (loan.outstandingPrincipal === 0) loan.status = 'Settled';
  audit('PAYMENT_RECEIVED', 'Payment', payment.id);
  res.status(201).json({ data: payment });
});

// ═══════════════════════════════════════════════════════════
// BACK OFFICE
// ═══════════════════════════════════════════════════════════
app.post('/api/v1/backoffice/applications', (req, res) => {
  const { productId, requestedAmount, requestedTenure, fullName, nationalId, employer, monthlySalary, agentId, dateOfBirth, address, city, region, employmentType, employmentStartDate, grossIncome, otherIncome } = req.body;
  const product = db.products.find(x => x.id === productId);
  if (!product) return res.status(400).json({ type: 'validation', title: 'Invalid product', status: 400 });
  const a = { id: randomUUID(), userId: randomUUID(), productId, productName: product.name, status: 'Draft', requestedAmount, approvedAmount: null, tenure: requestedTenure, income: monthlySalary, grossIncome: grossIncome || monthlySalary, otherIncome: otherIncome || 0, employerName: employer, employmentType: employmentType || null, employmentStartDate: employmentStartDate || null, fullName, nationalId, dateOfBirth: dateOfBirth || null, address: address || null, city: city || null, region: region || null, isListed: true, salaryDate: null, channel: 'BackOffice', agentId, createdAt: new Date().toISOString() };
  db.applications.push(a);
  audit('BO_APPLICATION_CREATED', 'Application', a.id, agentId);
  res.status(201).json({ data: a });
});

// ═══════════════════════════════════════════════════════════
// AUDIT LOG
// ═══════════════════════════════════════════════════════════
app.get('/api/v1/audit', (_, res) => res.json({ data: db.auditLogs.slice(-50) }));

// ═══════════════════════════════════════════════════════════
// START
// ═══════════════════════════════════════════════════════════
const PORT = 5000;
app.listen(PORT, () => console.log(`\n🏦 Tasheel Finance API running at http://localhost:${PORT}\n📋 Endpoints:\n   GET  /health\n   GET  /api/v1/products\n   POST /api/v1/applications\n   POST /api/v1/applications/:id/submit\n   POST /api/v1/applications/:id/assess\n   POST /api/v1/applications/:id/offer/generate\n   POST /api/v1/applications/:id/offer/accept\n   POST /api/v1/applications/:id/card\n   GET  /api/v1/loans\n   GET  /api/v1/loans/:id/settlement-figure\n   GET  /api/v1/loans/:id/liability-letter\n   GET  /api/v1/loans/:id/topup-eligibility\n   POST /api/v1/loans/:id/payments\n   POST /api/v1/backoffice/applications\n   GET  /api/v1/audit\n`));
