// frontend/src/services/api/customerApi.js
import axiosInstance from '../utils/axiosConfig';

const CUSTOMER_API = '/api/customers';

export const customerApi = {
  getCustomers: (branchId) =>
    axiosInstance.get(CUSTOMER_API, { params: { branchId } }),

  getCustomerById: (customerId) =>
    axiosInstance.get(`${CUSTOMER_API}/${customerId}`),

  createCustomer: (customerData) =>
    axiosInstance.post(CUSTOMER_API, customerData),

  updateCustomer: (customerId, customerData) =>
    axiosInstance.put(`${CUSTOMER_API}/${customerId}`, customerData),

  deleteCustomer: (customerId) =>
    axiosInstance.delete(`${CUSTOMER_API}/${customerId}`),

  getCustomerLoyaltyInfo: (customerId) =>
    axiosInstance.get(`${CUSTOMER_API}/${customerId}/loyalty`),
};
