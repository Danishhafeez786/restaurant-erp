// frontend/src/services/api/orderApi.js
import axiosInstance from '../utils/axiosConfig';

const ORDER_API = '/api/orders';

export const orderApi = {
  // Get all orders
  getOrders: (branchId) =>
    axiosInstance.get(ORDER_API, { params: { branchId } }),

  // Get order by ID
  getOrderById: (orderId) =>
    axiosInstance.get(`${ORDER_API}/${orderId}`),

  // Create new order
  createOrder: (orderData) =>
    axiosInstance.post(ORDER_API, orderData),

  // Update order
  updateOrder: (orderId, orderData) =>
    axiosInstance.put(`${ORDER_API}/${orderId}`, orderData),

  // Update order status
  updateOrderStatus: (orderId, status) =>
    axiosInstance.put(`${ORDER_API}/${orderId}/status`, { status }),

  // Cancel order
  cancelOrder: (orderId) =>
    axiosInstance.delete(`${ORDER_API}/${orderId}`),

  // Apply discount
  applyDiscount: (orderId, discountAmount) =>
    axiosInstance.post(`${ORDER_API}/${orderId}/apply-discount`, { discountAmount }),

  // Get billing details
  getBillingDetails: (orderId) =>
    axiosInstance.get(`${ORDER_API}/${orderId}/billing`),
};
