
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.assignment.Assignment;
import acme.realms.crew.Crew;

@GuiService
public class CrewActivityLogShowService extends AbstractGuiService<Crew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ActivityLog activityLog;
		int assignmentId;
		Crew member;

		assignmentId = super.getRequest().getData("assignmentId", int.class);
		activityLog = this.repository.findActivityLogById(assignmentId);
		member = activityLog == null ? null : activityLog.getAssignment().getCrew();
		status = member != null && (!activityLog.isDraftMode() || super.getRequest().getPrincipal().hasRealm(member));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int assignmentId;

		assignmentId = super.getRequest().getData("assignmentId", int.class);
		activityLog = this.repository.findActivityLogById(assignmentId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		SelectChoices selectedAssignments;
		Collection<Assignment> assignments;
		Crew member;

		member = (Crew) super.getRequest().getPrincipal().getActiveRealm();
		assignments = this.repository.findActivityLogPublishedByCrewId(member.getId());
		selectedAssignments = SelectChoices.from(assignments, "leg.flightNumber", activityLog.getAssignment());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeIncident", "description", "severityLevel", "draftMode");
		dataset.put("assignments", selectedAssignments);
		dataset.put("assignment", selectedAssignments.getSelected().getKey());
		dataset.put("assignmentId", activityLog.getAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
