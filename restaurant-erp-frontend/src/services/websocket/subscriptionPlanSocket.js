import { connectSocket, disconnectSocket } from "./websocketClient";

const TOPIC = "/topic/subscription-plans";

export const connectSubscriptionPlanSocket = (callback) => {
  connectSocket(TOPIC, callback);
};

export const disconnectSubscriptionPlanSocket = () => {
  disconnectSocket();
};
