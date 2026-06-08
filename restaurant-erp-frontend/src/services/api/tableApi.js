// frontend/src/services/api/tableApi.js
import axiosInstance from '../utils/axiosConfig';

const TABLE_API = '/api/tables';

export const tableApi = {
  // Get table by ID
  getTableById: (tableId) =>
    axiosInstance.get(`${TABLE_API}/${tableId}`),

  // Get tables by floor
  getTablesByFloor: (branchId, floorId) =>
    axiosInstance.get(`${TABLE_API}/floor/${floorId}`, { params: { branchId } }),

  // Get available tables
  getAvailableTables: (branchId) =>
    axiosInstance.get(`${TABLE_API}/available`, { params: { branchId } }),

  // Update table status
  updateTableStatus: (tableId, status) =>
    axiosInstance.put(`${TABLE_API}/${tableId}/status`, { status }),

  // Get floors
  getFloors: (branchId) =>
    axiosInstance.get(`${TABLE_API}/floors`, { params: { branchId } }),

  // Create floor
  createFloor: (floorData) =>
    axiosInstance.post(`${TABLE_API}/floors`, floorData),
};
