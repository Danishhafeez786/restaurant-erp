// frontend/src/services/api/attendanceApi.js
import axiosInstance from '../utils/axiosConfig';

const ATTENDANCE_API = '/api/attendance';

export const attendanceApi = {
  // Check in employee
  checkIn: (employeeId, employeeName, branchId, checkInMethod) =>
    axiosInstance.post(`${ATTENDANCE_API}/check-in`, {
      employeeId,
      employeeName,
      branchId,
      checkInMethod,
    }),

  // Check out employee
  checkOut: (attendanceId) =>
    axiosInstance.post(`${ATTENDANCE_API}/check-out`, { attendanceId }),

  // Get attendance records
  getAttendance: (branchId, startDate, endDate) =>
    axiosInstance.get(ATTENDANCE_API, {
      params: { branchId, startDate, endDate },
    }),

  // Get attendance report
  getAttendanceReport: (branchId, startDate, endDate) =>
    axiosInstance.get(`${ATTENDANCE_API}/report`, {
      params: { branchId, startDate, endDate },
    }),

  // Get employee late arrivals
  getLateArrivals: (branchId, startDate, endDate) =>
    axiosInstance.get(`${ATTENDANCE_API}/late-arrivals`, {
      params: { branchId, startDate, endDate },
    }),
};
