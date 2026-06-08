// frontend/src/services/api/payrollApi.js
import axiosInstance from '../utils/axiosConfig';

const PAYROLL_API = '/api/payroll';

export const payrollApi = {
  getSalaries: (branchId) =>
    axiosInstance.get(`${PAYROLL_API}/salaries`, { params: { branchId } }),

  calculatePayroll: (branchId, startDate, endDate) =>
    axiosInstance.post(`${PAYROLL_API}/calculate`, {
      branchId,
      startDate,
      endDate,
    }),

  getSalarySlip: (salarySlipId) =>
    axiosInstance.get(`${PAYROLL_API}/slips/${salarySlipId}`),

  getPayrollReport: (branchId, month, year) =>
    axiosInstance.get(`${PAYROLL_API}/report`, {
      params: { branchId, month, year },
    }),

  generatePayslips: (branchId, startDate, endDate) =>
    axiosInstance.post(`${PAYROLL_API}/generate-slips`, {
      branchId,
      startDate,
      endDate,
    }),
};
