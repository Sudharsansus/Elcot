@startuml AVGC-XR System Context
!theme plain

title Tamil Nadu AVGC-XR Portal - System Context Diagram

person "Citizen / Applicant" as applicant
person "Scheme Reviewer" as reviewer
person "ELCOT Admin" as admin
person "Content Editor" as editor

system "AVGC-XR Public Portal" as public_portal
system "AVGC-XR Applicant Portal" as applicant_portal
system "AVGC-XR Admin Portal" as admin_portal

system "AVGC-XR API" as api

actor "NIC SMS Gateway" as sms
actor "SMTP Email Server" as email
actor "Payment Gateway\n(SBI / HDFC)" as payment
actor "Aadhaar eKYC\n(UIDAI)" as ekyc

system "Strapi CMS" as cms
system "MinIO\nObject Storage" as minio

applicant --> public_portal : Browses schemes
applicant --> applicant_portal : Submits applications
reviewer --> admin_portal : Reviews applications
admin --> admin_portal : Manages schemes
editor --> cms : Manages content

public_portal --> api : REST API
applicant_portal --> api : REST API (JWT)
admin_portal --> api : REST API (JWT)

api --> sms : Send OTP / notifications
api --> email : Send emails
api --> payment : Payment processing
api --> ekyc : Identity verification
api --> cms : Fetch content
api --> minio : Store / retrieve files
@enduml