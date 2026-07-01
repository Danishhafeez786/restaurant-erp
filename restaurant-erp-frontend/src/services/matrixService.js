import axiosConfig from "./utils/axiosConfig";

const BASE_URL = "/role-permission";

const matrixService = {

    async getMatrix(organizationId = null) {

        let url = `${BASE_URL}/matrix`;

        if (organizationId) {
            url += `?organizationId=${organizationId}`;
        }

        const response = await axiosConfig.get(url);

        return response.data;

    }

};

export default matrixService;