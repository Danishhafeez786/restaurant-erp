import { connectSocket, disconnectSocket } from "./websocketClient";

const TOPIC = "/topic/organizations";

export const connectOrganizationSocket = (callback) => {
  connectSocket(TOPIC, callback);
};

export const disconnectOrganizationSocket = () => {
  disconnectSocket();
};
