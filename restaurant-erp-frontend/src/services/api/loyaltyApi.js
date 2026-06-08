// frontend/src/services/api/loyaltyApi.js
import axiosInstance from '../utils/axiosConfig';

const LOYALTY_API = '/api/loyalty';

export const loyaltyApi = {
  getLoyaltyAccount: (customerId) =>
    axiosInstance.get(`${LOYALTY_API}/accounts/${customerId}`),

  getTransactions: (customerId) =>
    axiosInstance.get(`${LOYALTY_API}/transactions`, { params: { customerId } }),

  redeemPoints: (customerId, points, orderId) =>
    axiosInstance.post(`${LOYALTY_API}/redeem`, {
      customerId,
      points,
      orderId,
    }),

  addPoints: (customerId, points, reason) =>
    axiosInstance.post(`${LOYALTY_API}/add-points`, {
      customerId,
      points,
      reason,
    }),

  getLoyaltyReport: (branchId, startDate, endDate) =>
    axiosInstance.get(`${LOYALTY_API}/report`, {
      params: { branchId, startDate, endDate },
    }),
};
