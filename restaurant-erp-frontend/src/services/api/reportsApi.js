// frontend/src/services/api/reportsApi.js
import axiosInstance from '../utils/axiosConfig';

const REPORTS_API = '/api/reports';

export const reportsApi = {
  getSalesReport: (branchId, startDate, endDate) =>
    axiosInstance.get(`${REPORTS_API}/sales`, {
      params: { branchId, startDate, endDate },
    }),

  getInventoryReport: (branchId) =>
    axiosInstance.get(`${REPORTS_API}/inventory`, {
      params: { branchId },
    }),

  getStaffReport: (branchId, startDate, endDate) =>
    axiosInstance.get(`${REPORTS_API}/staff`, {
      params: { branchId, startDate, endDate },
    }),

  getCustomerReport: (branchId, startDate, endDate) =>
    axiosInstance.get(`${REPORTS_API}/customers`, {
      params: { branchId, startDate, endDate },
    }),

  getItemPopularity: (branchId, startDate, endDate) =>
    axiosInstance.get(`${REPORTS_API}/item-popularity`, {
      params: { branchId, startDate, endDate },
    }),

  getDailyRevenue: (branchId, startDate, endDate) =>
    axiosInstance.get(`${REPORTS_API}/daily-revenue`, {
      params: { branchId, startDate, endDate },
    }),

  exportReport: (reportType, branchId, format = 'pdf') =>
    axiosInstance.get(`${REPORTS_API}/export`, {
      params: { reportType, branchId, format },
      responseType: 'blob',
    }),
};
