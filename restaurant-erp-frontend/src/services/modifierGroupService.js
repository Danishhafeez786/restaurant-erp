import axiosConfig from "./utils/axiosConfig";

const BASE_URL = "/modifier-group";

const modifierGroupService = {

    search: async (
        criteria = {},
        page = 0,
        size = 25,
        sortBy = "createdAt",
        direction = "DESC"
    ) => {

        const response = await axiosConfig.post(
            `${BASE_URL}/search?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`,
            criteria
        );

        return response.data;
    },

    create: async (payload) => {

        const response = await axiosConfig.post(
            BASE_URL,
            payload
        );

        return response.data;
    },

    update: async (id, payload) => {

        const response = await axiosConfig.put(
            `${BASE_URL}/${id}`,
            payload
        );

        return response.data;
    },

    delete: async (id) => {

        const response = await axiosConfig.delete(
            `${BASE_URL}/${id}`
        );

        return response.data;
    },

    restore: async (id) => {

        const response = await axiosConfig.patch(
            `${BASE_URL}/${id}/restore`
        );

        return response.data;
    },

    getById: async (id) => {

        const response = await axiosConfig.get(
            `${BASE_URL}/${id}`
        );

        return response.data;
    },

    stream() {

        return new EventSource(
            `${import.meta.env.VITE_API_URL}/modifier-group/stream`
        );

    }

};

export default modifierGroupService;