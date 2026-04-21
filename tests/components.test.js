import { describe, test, expect, beforeEach } from '@jest/globals';
import { JSDOM } from 'jsdom';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const html = fs.readFileSync(path.join(__dirname, '../../deploy/index.html'), 'utf-8');
let dom, document;

beforeEach(() => { dom = new JSDOM(html); document = dom.window.document; });

describe('Design System Tokens', () => {
  test('CSS variables are defined on :root', () => {
    const style = document.querySelector('style').textContent;
    expect(style).toContain('--primary-action');
    expect(style).toContain('--neutral-900');
    expect(style).toContain('--border-input');
  });
  test('--primary-action is #4f6ef7', () => {
    expect(document.querySelector('style').textContent).toMatch(/--primary-action\s*:\s*#4f6ef7/);
  });
  test('--neutral-900 is #292c3d', () => {
    expect(document.querySelector('style').textContent).toMatch(/--neutral-900\s*:\s*#292c3d/);
  });
  test('--radius-md is 6px (card border radius)', () => {
    expect(document.querySelector('style').textContent).toMatch(/--radius-md\s*:\s*6px/);
  });
  test('--spacing-4x is 16px and --font-size-md is 14px', () => {
    const s = document.querySelector('style').textContent;
    expect(s).toMatch(/--spacing-4x\s*:\s*16px/);
    expect(s).toMatch(/--font-size-md\s*:\s*14px/);
  });
  test('--border-input is #858aad (from input.component.scss)', () => {
    expect(document.querySelector('style').textContent).toMatch(/--border-input\s*:\s*#858aad/);
  });
  test('--radius-lg is 8px (input border radius from SCSS --3x)', () => {
    expect(document.querySelector('style').textContent).toMatch(/--radius-lg\s*:\s*8px/);
  });
});

describe('Screen Structure (matching wireframes)', () => {
  test('A. Login screen exists with phone, password, biometric', () => {
    const login = document.getElementById('screen-login');
    expect(login).not.toBeNull();
    expect(document.getElementById('f-login-phone')).not.toBeNull();
    expect(document.getElementById('f-login-pass')).not.toBeNull();
    expect(document.getElementById('f-login-pass').type).toBe('password');
    expect(login.textContent).toContain('Biometric');
    expect(login.textContent).toContain('Forgot password');
  });
  test('B. Product list screen exists with bottom nav', () => {
    const products = document.getElementById('screen-products');
    expect(products).not.toBeNull();
    expect(products.querySelector('.bottom-nav')).not.toBeNull();
    const navItems = products.querySelectorAll('.ni');
    expect(navItems.length).toBe(3);
    expect(navItems[0].textContent).toContain('Home');
    expect(navItems[1].textContent).toContain('Products');
    expect(navItems[2].textContent).toContain('Profile');
  });
  test('C. Application form has 4 steps (Personal, Employment, Income, Review)', () => {
    expect(document.getElementById('apply-1')).not.toBeNull(); // Personal
    expect(document.getElementById('apply-2')).not.toBeNull(); // Employment
    expect(document.getElementById('apply-3')).not.toBeNull(); // Income
    expect(document.getElementById('apply-4')).not.toBeNull(); // Review
  });
  test('C1. Personal Info has name, DOB, national ID, address, city, region', () => {
    expect(document.getElementById('f-name')).not.toBeNull();
    expect(document.getElementById('f-dob')).not.toBeNull();
    expect(document.getElementById('f-nid')).not.toBeNull();
    expect(document.getElementById('f-address')).not.toBeNull();
    expect(document.getElementById('f-city')).not.toBeNull();
    expect(document.getElementById('f-region')).not.toBeNull();
  });
  test('C2. Employment has type dropdown, employer, start date', () => {
    expect(document.getElementById('f-emptype').tagName).toBe('SELECT');
    expect(document.getElementById('f-employer')).not.toBeNull();
    expect(document.getElementById('f-empstart')).not.toBeNull();
  });
  test('C3. Income has gross, net, other, amount, tenure', () => {
    expect(document.getElementById('f-gross')).not.toBeNull();
    expect(document.getElementById('f-salary')).not.toBeNull();
    expect(document.getElementById('f-other')).not.toBeNull();
    expect(document.getElementById('f-amount')).not.toBeNull();
    expect(document.getElementById('f-tenure')).not.toBeNull();
  });
  test('C4. Review has edit buttons and terms checkbox', () => {
    const review = document.getElementById('apply-4');
    const edits = review.querySelectorAll('.edit');
    expect(edits.length).toBeGreaterThanOrEqual(3); // Personal, Employment, Income
    expect(review.textContent).toContain('Terms & Conditions');
  });
  test('D. Card screen has card number, expiry, CVV, salary date, authorization checkbox', () => {
    expect(document.getElementById('f-card')).not.toBeNull();
    expect(document.getElementById('f-expiry')).not.toBeNull();
    expect(document.getElementById('f-cvv')).not.toBeNull();
    expect(document.getElementById('f-cvv').type).toBe('password');
    expect(document.getElementById('f-adday')).not.toBeNull();
    expect(document.getElementById('f-adday').min).toBe('1');
    expect(document.getElementById('f-adday').max).toBe('28');
    const cardScreen = document.getElementById('screen-card');
    expect(cardScreen.textContent).toContain('authorize');
  });
  test('E. Offer screen has amount, details, accept/decline buttons', () => {
    const offer = document.getElementById('screen-offer');
    expect(offer).not.toBeNull();
    expect(document.getElementById('offer-amount')).not.toBeNull();
    expect(document.getElementById('offer-details')).not.toBeNull();
    expect(offer.textContent).toContain('Accept Offer');
    expect(offer.textContent).toContain('Decline');
    expect(offer.textContent).toContain('valid for 48 hours');
  });
  test('F. Dashboard has welcome message, loans area, bottom nav', () => {
    const dash = document.getElementById('screen-dashboard');
    expect(dash).not.toBeNull();
    expect(document.getElementById('welcome-msg')).not.toBeNull();
    expect(document.getElementById('dashboard-loans')).not.toBeNull();
    expect(dash.querySelector('.bottom-nav')).not.toBeNull();
  });
  test('G. Loan detail has balance, details, payment schedule, settlement/liability/topup buttons', () => {
    const loan = document.getElementById('screen-loan');
    expect(loan).not.toBeNull();
    expect(document.getElementById('loan-balance')).not.toBeNull();
    expect(document.getElementById('loan-details')).not.toBeNull();
    expect(document.getElementById('loan-schedule')).not.toBeNull();
    expect(loan.textContent).toContain('Settlement Figure');
    expect(loan.textContent).toContain('Liability Letter');
    expect(loan.textContent).toContain('Top-Up Eligibility');
  });
});

describe('Navigation', () => {
  test('Login screen is visible initially, others hidden', () => {
    expect(document.getElementById('screen-login').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('screen-products').classList.contains('hidden')).toBe(true);
    expect(document.getElementById('screen-apply').classList.contains('hidden')).toBe(true);
    expect(document.getElementById('screen-dashboard').classList.contains('hidden')).toBe(true);
  });
  test('All 7 screens exist', () => {
    ['login','products','apply','card','offer','dashboard','loan'].forEach(s => {
      expect(document.getElementById('screen-'+s)).not.toBeNull();
    });
  });
});

describe('Form Defaults', () => {
  test('Login has pre-filled values', () => {
    expect(document.getElementById('f-login-phone').value).toBe('ahmed@test.com');
  });
  test('Personal info has pre-filled values', () => {
    expect(document.getElementById('f-name').value).toBe('Ahmed Al-Rashid');
    expect(document.getElementById('f-nid').value).toBe('1234567890');
    expect(document.getElementById('f-dob').value).toBe('1985-03-15');
    expect(document.getElementById('f-address').value).toBe('King Fahd Road');
    expect(document.getElementById('f-city').value).toBe('Riyadh');
  });
  test('Employment has pre-filled values', () => {
    expect(document.getElementById('f-employer').value).toBe('Saudi Aramco');
    expect(document.getElementById('f-emptype').value).toBe('Government');
  });
  test('Income has pre-filled values', () => {
    expect(document.getElementById('f-salary').value).toBe('13500');
    expect(document.getElementById('f-amount').value).toBe('50000');
    expect(document.getElementById('f-tenure').value).toBe('24');
  });
  test('Card has pre-filled values', () => {
    expect(document.getElementById('f-card').value).toBe('4111111111111234');
    expect(document.getElementById('f-adday').value).toBe('25');
  });
});

describe('Accessibility', () => {
  test('All form inputs have associated labels', () => {
    const inputs = document.querySelectorAll('.field input, .field select');
    inputs.forEach(input => {
      const field = input.closest('.field');
      expect(field.querySelector('label')).not.toBeNull();
    });
  });
  test('All buttons have text content', () => {
    document.querySelectorAll('.btn, .btn-text').forEach(btn => {
      expect(btn.textContent.trim().length).toBeGreaterThan(0);
    });
  });
  test('CVV and password inputs are masked', () => {
    expect(document.getElementById('f-cvv').type).toBe('password');
    expect(document.getElementById('f-login-pass').type).toBe('password');
  });
});

describe('API Integration', () => {
  test('API base URL is localhost:5000', () => {
    expect(document.querySelector('script').textContent).toContain("'http://localhost:5000'");
  });
  test('State tracks productId, appId, loanId', () => {
    const script = document.querySelector('script').textContent;
    expect(script).toMatch(/state\s*=\s*\{[^}]*productId/);
    expect(script).toMatch(/state\s*=\s*\{[^}]*appId/);
    expect(script).toMatch(/state\s*=\s*\{[^}]*loanId/);
  });
});
