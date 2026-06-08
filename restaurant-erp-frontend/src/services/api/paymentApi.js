// frontend/src/services/api/paymentApi.js
import axiosInstance from '../utils/axiosConfig';

const PAYMENT_API = '/api/payments';

export const paymentApi = {
  // Process payment
  processPayment: (paymentData) =>
    axiosInstance.post(`${PAYMENT_API}/process`, paymentData),

  // Get payments
  getPayments: (branchId) =>
    axiosInstance.get(PAYMENT_API, { params: { branchId } }),

  // Get payment by ID
  getPaymentById: (paymentId) =>
    axiosInstance.get(`${PAYMENT_API}/${paymentId}`),

  // Process refund
  processRefund: (paymentId) =>
    axiosInstance.post(`${PAYMENT_API}/${paymentId}/refund`, {}),

  // Get cash drawer
  getCashDrawer: (branchId) =>
    axiosInstance.get('/api/cash-drawer', { params: { branchId } }),

  // Reconcile cash
  reconcileCash: (branchId, reconciliationData) =>
    axiosInstance.post('/api/cash-drawer/reconcile', {
      branchId,
      ...reconciliationData,
    }),
};
