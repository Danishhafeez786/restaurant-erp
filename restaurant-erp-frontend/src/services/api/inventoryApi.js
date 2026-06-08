// frontend/src/services/api/inventoryApi.js
import axiosInstance from '../utils/axiosConfig';

const INVENTORY_API = '/api/inventory';

export const inventoryApi = {
  // Get inventory by branch
  getInventory: (branchId) =>
    axiosInstance.get(INVENTORY_API, { params: { branchId } }),

  // Get inventory item by ID
  getInventoryById: (inventoryId) =>
    axiosInstance.get(`${INVENTORY_API}/${inventoryId}`),

  // Adjust stock
  adjustStock: (inventoryId, quantity, movementType, notes, movedBy) =>
    axiosInstance.post(`${INVENTORY_API}/adjust-stock`, {
      inventoryId,
      quantity,
      movementType,
      notes,
      movedBy,
    }),

  // Get stock movements
  getStockMovements: (branchId) =>
    axiosInstance.get(`${INVENTORY_API}/movements`, { params: { branchId } }),

  // Get low stock alerts
  getLowStockAlerts: (branchId) =>
    axiosInstance.get(`${INVENTORY_API}/alerts`, { params: { branchId } }),
};
