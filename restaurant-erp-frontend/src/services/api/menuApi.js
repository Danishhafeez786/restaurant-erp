// frontend/src/services/api/menuApi.js
import axiosInstance from '../utils/axiosConfig';

const MENU_API = '/api/menu';

export const menuApi = {
  // Categories
  getCategories: (branchId) =>
    axiosInstance.get(`${MENU_API}/categories`, { params: { branchId } }),

  createCategory: (categoryData) =>
    axiosInstance.post(`${MENU_API}/categories`, categoryData),

  // Menu Items
  getMenuItems: (branchId) =>
    axiosInstance.get(`${MENU_API}/items`, { params: { branchId } }),

  getMenuItemById: (itemId) =>
    axiosInstance.get(`${MENU_API}/items/${itemId}`),

  createMenuItem: (menuItemData) =>
    axiosInstance.post(`${MENU_API}/items`, menuItemData),

  updateMenuItem: (itemId, menuItemData) =>
    axiosInstance.put(`${MENU_API}/items/${itemId}`, menuItemData),

  deleteMenuItem: (itemId) =>
    axiosInstance.delete(`${MENU_API}/items/${itemId}`),

  // Modifiers
  getModifiers: (itemId) =>
    axiosInstance.get(`${MENU_API}/modifiers`, { params: { itemId } }),

  // Combo Deals
  getCombos: (branchId) =>
    axiosInstance.get(`${MENU_API}/combo-deals`, { params: { branchId } }),

  createCombo: (comboData) =>
    axiosInstance.post(`${MENU_API}/combo-deals`, comboData),
};
