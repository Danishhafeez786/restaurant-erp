import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client/dist/sockjs";

let stompClient = null;

export const connectSocket = (topic, onMessage) => {
  const socket = new SockJS("http://192.168.24.68:8080/ws");

  stompClient = new Client({
    webSocketFactory: () => socket,

    reconnectDelay: 5000,

    onConnect: () => {
      console.log("WebSocket Connected");

      stompClient.subscribe(topic, (message) => {
        const event = JSON.parse(message.body);

        onMessage(event);
      });
    },

    onStompError: (frame) => {
      console.error("WebSocket Error", frame);
    },
  });

  stompClient.activate();
};

export const disconnectSocket = () => {
  if (stompClient) {
    stompClient.deactivate();

    console.log("WebSocket Disconnected");
  }
};
