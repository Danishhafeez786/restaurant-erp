// frontend/src/services/api/kitchenApi.js
import axiosInstance from '../utils/axiosConfig';

const KITCHEN_API = '/api/kitchen';

export const kitchenApi = {
  // Get kitchen orders
  getKitchenOrders: (branchId) =>
    axiosInstance.get(`${KITCHEN_API}/orders`, { params: { branchId } }),

  // Update kitchen order status
  updateOrderStatus: (orderId, status) =>
    axiosInstance.put(`${KITCHEN_API}/orders/${orderId}/status`, { status }),

  // Assign order to chef
  assignOrderToChef: (orderId, chefId, chefName) =>
    axiosInstance.post(`${KITCHEN_API}/orders/${orderId}/assign`, { chefId, chefName }),

  // Get kitchen stations
  getStations: (branchId) =>
    axiosInstance.get(`${KITCHEN_API}/stations`, { params: { branchId } }),

  // Update station status
  updateStationStatus: (stationId, status) =>
    axiosInstance.put(`${KITCHEN_API}/stations/${stationId}/status`, { status }),
};
