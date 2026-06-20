import axios from 'axios';

const API_BASE_URL = 'http://192.168.100.3:8080/api/auth';

export const authApi = {
  signup: async (data) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/signup`, data, {
        headers: {
          'Content-Type': 'application/json',
        },
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Network error' };
    }
  },

  login: async (data) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/login`, data, {
        headers: {
          'Content-Type': 'application/json',
        },
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Network error' };
    }
  },

  test: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/test`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Network error' };
    }
  },
};
