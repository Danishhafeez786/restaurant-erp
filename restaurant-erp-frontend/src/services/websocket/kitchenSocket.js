// frontend/src/services/websocket/kitchenSocket.js
/**
 * Kitchen WebSocket Service
 * Manages real-time kitchen operations
 */

class KitchenSocket {
  constructor() {
    this.socket = null;
    this.isConnected = false;
    this.messageHandlers = {};
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 10;
  }

  connect(branchId, userId) {
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
    const url = `${protocol}://${window.location.host}/ws/kitchen?branchId=${branchId}&userId=${userId}`;

    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('Kitchen WebSocket connected');
      this.isConnected = true;
      this.reconnectAttempts = 0;
      this.subscribe(branchId);
    };

    this.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      this.handleMessage(message);
    };

    this.socket.onerror = (error) => {
      console.error('Kitchen WebSocket error:', error);
    };

    this.socket.onclose = () => {
      console.log('Kitchen WebSocket closed');
      this.isConnected = false;
      this.attemptReconnect(branchId, userId);
    };
  }

  subscribe(branchId) {
    this.send({
      action: 'SUBSCRIBE',
      channel: `kitchen_${branchId}`,
    });
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

  off(messageType) {
    delete this.messageHandlers[messageType];
  }

  updateOrderStatus(orderId, status, notes = '') {
    this.send({
      type: 'ORDER_STATUS_UPDATE',
      orderId,
      status,
      notes,
      timestamp: new Date().toISOString(),
    });
  }

  markOrderReady(orderId) {
    this.updateOrderStatus(orderId, 'READY');
  }

  markOrderUrgent(orderId) {
    this.send({
      type: 'ORDER_URGENT',
      orderId,
      timestamp: new Date().toISOString(),
    });
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
    }
  }

  attemptReconnect(branchId, userId) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      const delay = Math.pow(2, this.reconnectAttempts) * 1000;
      setTimeout(() => {
        this.reconnectAttempts++;
        console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
        this.connect(branchId, userId);
      }, delay);
    }
  }
}

export const kitchenSocket = new KitchenSocket();
