package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Column(precision = 10, scale = 6)
	private BigDecimal deliveryLatitude;
	@Column(precision = 10, scale = 6)
	private BigDecimal deliveryLongitude;

	public BigDecimal getDeliveryLatitude() { return deliveryLatitude; }
	public void setDeliveryLatitude(BigDecimal deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }
	public BigDecimal getDeliveryLongitude() { return deliveryLongitude; }
	public void setDeliveryLongitude(BigDecimal deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }
	private LocalDateTime scheduledDeliveryTime;

	public LocalDateTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
	public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
	@Column(precision = 10, scale = 2)
	private BigDecimal tip;

	public BigDecimal getTip() { return tip; }
	public void setTip(BigDecimal tip) { this.tip = tip; }
	@Column(length = 100)
	private String promoCode;

	public String getPromoCode() { return promoCode; }
	public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	public PaymentMethod getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
	@Builder.Default
	private BigDecimal total = BigDecimal.ZERO;

	public BigDecimal getTotal() { return total; }
	public void setTotal(BigDecimal total) { this.total = total; }
	@Column(precision = 10, scale = 2)
	private BigDecimal deliveryFee;

	@Column(length = 255)
	private String deliveryAddress;

	private Double latitude;
	private Double longitude;

	private LocalDateTime estimatedDeliveryTime;

	@Column(length = 1000)
	private String notes;

	@Builder.Default
	private Integer rating = 0;

	@Column(length = 1000)
	private String reviewComment;

	private LocalDateTime reviewedAt;

	private LocalDateTime confirmedAt;
	private LocalDateTime cancelledAt;

	@ElementCollection
	@CollectionTable(name = "order_status_history", joinColumns = @JoinColumn(name = "order_id"))
	@Column(name = "status")
	private java.util.List<String> statusHistory;

	private String cancellationReason;

	public BigDecimal getDeliveryFee() { return deliveryFee; }
	public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
	public String getDeliveryAddress() { return deliveryAddress; }
	public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
	public Double getLatitude() { return latitude; }
	public void setLatitude(Double latitude) { this.latitude = latitude; }
	public Double getLongitude() { return longitude; }
	public void setLongitude(Double longitude) { this.longitude = longitude; }
	public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
	public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	public Integer getRating() { return rating; }
	public void setRating(Integer rating) { this.rating = rating; }
	public String getReviewComment() { return reviewComment; }
	public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
	public LocalDateTime getReviewedAt() { return reviewedAt; }
	public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
	public LocalDateTime getConfirmedAt() { return confirmedAt; }
	public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
	public LocalDateTime getCancelledAt() { return cancelledAt; }
	public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
	public java.util.List<String> getStatusHistory() { return statusHistory; }
	public void setStatusHistory(java.util.List<String> statusHistory) { this.statusHistory = statusHistory; }
	public String getCancellationReason() { return cancellationReason; }
	public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
	@Column(length = 100, unique = true)
	private String orderNumber;

	@Column(precision = 10, scale = 2)
	private BigDecimal subtotal;

	@Enumerated(EnumType.STRING)
	private DeliveryMethod deliveryMethod;

	private LocalDateTime deliveredAt;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public void setStatus(OrderStatus status) {
		this.status = status.name();
	}

	public enum DeliveryMethod {
		PICKUP,
		DELIVERY
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private BigDecimal totalAmount;

	@Column(nullable = false)
	private String status;

	public static class OrderBuilder {
		public OrderBuilder deliveryLatitude(BigDecimal deliveryLatitude) {
			this.deliveryLatitude = deliveryLatitude;
			return this;
		}
		public OrderBuilder deliveryLongitude(BigDecimal deliveryLongitude) {
			this.deliveryLongitude = deliveryLongitude;
			return this;
		}
		public OrderBuilder scheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) {
			this.scheduledDeliveryTime = scheduledDeliveryTime;
			return this;
		}
		public OrderBuilder tip(BigDecimal tip) {
			this.tip = tip;
			return this;
		}
		public OrderBuilder promoCode(String promoCode) {
			this.promoCode = promoCode;
			return this;
		}
		public OrderBuilder estimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
			this.estimatedDeliveryTime = estimatedDeliveryTime;
			return this;
		}
		public OrderBuilder paymentMethod(PaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}
		public OrderBuilder status(OrderStatus status) {
			this.status = status.name();
			return this;
		}
		public OrderBuilder latitude(Double latitude) {
			this.latitude = latitude;
			return this;
		}
		public OrderBuilder longitude(Double longitude) {
			this.longitude = longitude;
			return this;
		}
	}

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;
	// ...existing code...
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    public enum OrderStatus {
	PENDING,
	ACCEPTED,
	CONFIRMED,
	IN_PROGRESS,
	COMPLETED,
	DELIVERED,
	CANCELLED
	}

	public enum PaymentStatus {
		UNPAID,
		PAID,
		REFUNDED
	}

	public enum PaymentMethod {
		CASH,
		CREDIT_CARD,
		ONLINE
	}
}
