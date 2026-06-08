// frontend/src/services/api/branchApi.js
import axiosInstance from '../utils/axiosConfig';

const BRANCH_API = '/api/branches';

export const branchApi = {
  // Get all branches for organization
  getBranchesByOrganization: (organizationId) =>
    axiosInstance.get(`${BRANCH_API}/org/${organizationId}`),

  // Get branch by ID
  getBranchById: (branchId) =>
    axiosInstance.get(`${BRANCH_API}/${branchId}`),

  // Create new branch
  createBranch: (branchData) =>
    axiosInstance.post(BRANCH_API, branchData),

  // Update branch
  updateBranch: (branchId, branchData) =>
    axiosInstance.put(`${BRANCH_API}/${branchId}`, branchData),

  // Delete branch
  deleteBranch: (branchId) =>
    axiosInstance.delete(`${BRANCH_API}/${branchId}`),
};
