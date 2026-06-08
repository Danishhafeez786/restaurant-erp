import axiosClient from "../../api/axiosClient";

export const loginUser = async (data) => {
  const response = await axiosClient.post(
    "auth/login", 
    data
);

  return response.data;
};

export const signupUser = async (data) => {
  const response = await axiosClient.post(
    "auth/signup",
    data
  );

  return response.data;
};