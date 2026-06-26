# Workflow standard — BPMN 2.0 (and the BPEL 2.0 clause)

**Tender clause:** workflow engine supporting *BPMN 2.0 + BPEL 2.0*.
**What the portal implements:** **BPMN 2.0** via the Flowable engine (`flowable-spring-boot-starter-process`
7.2.0), with executable processes under `apps/api/src/main/resources/processes/`
(`applicationReview.bpmn`, `schemeApproval.bpmn`) and Java service delegates (e.g.
`RejectionNotificationDelegate`). The maker–checker–reviewer–approver lifecycle, task assignment, and
audit history are modelled as BPMN.

## Why BPMN 2.0 satisfies the intent

BPMN 2.0 and BPEL 2.0 solve **different** problems, and the tender's intent — a configurable,
auditable, human-centric approval workflow — is squarely BPMN's domain:

| Aspect | BPMN 2.0 (used here) | BPEL 2.0 |
|---|---|---|
| Purpose | Human + system **business process** orchestration | **Web-service (SOAP/WSDL)** orchestration |
| Human tasks (maker-checker-approver) | First-class (user tasks, assignees, forms) | Not native — needs the BPEL4People/WS-HumanTask extensions |
| Notation | Standardised visual diagram (reviewable by officials) | XML-only, no standard diagram |
| Ecosystem today | Active (Flowable, Camunda, Activiti) | Largely legacy; aimed at the SOAP/ESB era |
| REST/JSON microservices | Natural fit | Designed around WSDL/SOAP |

This portal is a REST/JSON system with heavy human approval steps, so BPMN 2.0 is the correct and
modern standard. BPEL 2.0 is an XML language for orchestrating **SOAP web services** and is rarely
deployed in greenfield government portals; most contemporary platforms (and most current ELCOT-class
stacks) standardise on BPMN 2.0.

## Bid narrative / options if BPEL is mandatory

1. **Preferred (recommended in the technical proposal):** state that the workflow requirement is met
   in full with **BPMN 2.0**, which additionally provides the standardised, official-reviewable
   diagrams and native human-task support that BPEL lacks. Note that the engine choice (Flowable) is
   an open, widely-supported standard with no lock-in.
2. **If the evaluation insists on literal BPEL 2.0 coexistence:** Flowable/Activiti historically
   shipped a BPEL-to-BPMN bridge, and a standalone Apache ODE (Orchestration Director Engine) BPEL
   runtime can be added behind the same workflow service interface for any pure service-orchestration
   (SOAP) steps — to be scoped only if a concrete BPEL use-case is identified, since none of the
   portal's modules require SOAP orchestration today.

**Recommendation:** address this in the technical write-up (option 1); do not add a BPEL engine unless
the client identifies a real SOAP-orchestration requirement (option 2). Adding an unused BPEL runtime
would increase attack surface and maintenance for no functional gain.
