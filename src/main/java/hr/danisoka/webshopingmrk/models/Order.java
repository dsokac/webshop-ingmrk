package hr.danisoka.webshopingmrk.models;

import java.math.BigDecimal;
import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import hr.danisoka.webshopingmrk.MyEnumConverter;
import lombok.Getter;
import lombok.Setter;

@TypeDef(name = "MyEnumConverter", typeClass = MyEnumConverter.class)
@Entity @Getter @Setter @Table(name = "orders")
public class Order {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@Type(type = "MyEnumConverter")
	@Column(nullable = false)
	private Status status = Status.DRAFT;
	
	@Column(name = "total_price_hrk", nullable = false, precision = 8, scale = 2)
	private BigDecimal totalPriceHrk = new BigDecimal(0);
	
	@Column(name = "total_price_eur", nullable = false, precision = 9, scale = 2)
	private BigDecimal totalPriceEur = new BigDecimal(0);
	
	public Order() {}
	
	public Order(Customer customer) {
		this.customer = customer;
	}
	
	/*public enum Status {
		DRAFT("DRAFT"), SUBMITTED("SUBMITTED");
		
		private String statusName;
		private Status(String statusName) {
			this.statusName = statusName;
		}
		
		@Override
		public String toString() {
			return statusName;
		}
		
	}*/
	public enum Status {
		DRAFT, SUBMITTED;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((totalPriceEur == null) ? 0 : totalPriceEur.hashCode());
		result = prime * result + ((totalPriceHrk == null) ? 0 : totalPriceHrk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (totalPriceEur == null) {
			if (other.totalPriceEur != null)
				return false;
		} else if (!totalPriceEur.equals(other.totalPriceEur))
			return false;
		if (totalPriceHrk == null) {
			if (other.totalPriceHrk != null)
				return false;
		} else if (!totalPriceHrk.equals(other.totalPriceHrk))
			return false;
		return true;
	}	
}
