
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	protected String			model;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	protected String			registrationNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 255)
	@Automapped
	protected Integer			capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	@Automapped
	protected Integer			cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	protected ServiceStatus		status;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	protected String			details;

}
