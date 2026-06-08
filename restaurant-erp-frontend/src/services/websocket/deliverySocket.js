// frontend/src/services/websocket/deliverySocket.js
/**
 * Delivery Tracking WebSocket Service
 * Manages real-time delivery location updates
 */

class DeliverySocket {
  constructor() {
    this.socket = null;
    this.isConnected = false;
    this.messageHandlers = {};
    this.locationTrackingActive = {};
  }

  connect(branchId, userId) {
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
    const url = `${protocol}://${window.location.host}/ws/delivery?branchId=${branchId}&userId=${userId}`;

    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('Delivery WebSocket connected');
      this.isConnected = true;
    };

    this.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      this.handleMessage(message);
    };

    this.socket.onerror = (error) => {
      console.error('Delivery WebSocket error:', error);
    };

    this.socket.onclose = () => {
      console.log('Delivery WebSocket closed');
      this.isConnected = false;
    };
  }

  send(message) {
    if (this.socket && this.isConnected) {
      this.socket.send(JSON.stringify(message));
    }
  }

  handleMessage(message) {
    const { type } = message;
    if (this.messageHandlers[type]) {
      this.messageHandlers[type](message);
    }
  }

  on(messageType, handler) {
    this.messageHandlers[messageType] = handler;
  }

  updateLocation(deliveryId, latitude, longitude) {
    this.send({
      type: 'LOCATION_UPDATE',
      deliveryId,
      latitude,
      longitude,
      timestamp: new Date().toISOString(),
    });
  }

  updateDeliveryStatus(deliveryId, status) {
    this.send({
      type: 'DELIVERY_STATUS_UPDATE',
      deliveryId,
      status,
      timestamp: new Date().toISOString(),
    });
  }

  trackDelivery(deliveryId) {
    this.locationTrackingActive[deliveryId] = true;
    this.send({
      type: 'START_TRACKING',
      deliveryId,
    });
  }

  stopTracking(deliveryId) {
    this.locationTrackingActive[deliveryId] = false;
    this.send({
      type: 'STOP_TRACKING',
      deliveryId,
    });
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
    }
  }
}

export const deliverySocket = new DeliverySocket();
