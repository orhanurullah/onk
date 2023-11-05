package com.onk.model;

import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class BaseIdModel implements Cloneable, Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.tableId)
	private Long id;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BaseIdModel other = (BaseIdModel) obj;
		if (id == null) {
            return other.id == null;
		} else return id.equals(other.id);
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
