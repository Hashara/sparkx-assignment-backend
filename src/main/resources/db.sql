CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE district AS ENUM('district A', 'district B',  'district C',
    'district D', 'district E');

CREATE TABLE hospital
(
    hospitalId uuid DEFAULT uuid_generate_v4(),
    name       VARCHAR(45) NULL,
    district   district NOT NULL,
    location_x INT      NOT NULL,
    location_y INT      NOT NULL,
    PRIMARY KEY (hospitalId)
);

CREATE TYPE RoleType AS ENUM('MoH',
    'Director',
    'Doctor',
    'HospitalStaff',
    'Patient');


CREATE TABLE person
(
    userId     uuid DEFAULT uuid_generate_v4(),
    email      VARCHAR(30) NOT NULL UNIQUE,
    password   VARCHAR(64) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    hospitalId uuid NULL,
    role       RoleType    NOT NULL,
    PRIMARY KEY (userId),
    FOREIGN KEY (hospitalId) REFERENCES hospital (hospitalId)
);

CREATE TYPE genderTypes AS ENUM('male', 'female');

CREATE TABLE patient
(
    patientId  uuid     NOT NULL,
    district   district NOT NULL,
    location_x INT      NOT NULL,
    location_y INT      NOT NULL,
    gender     genderTypes NULL,
    contact    VARCHAR(10) NULL,
    birthDate  DATE,
    death      DATE,
    PRIMARY KEY (patientId),
    FOREIGN KEY (patientId) REFERENCES person (userId)
);

CREATE TYPE statusType AS ENUM('unavailable', 'available');

CREATE TABLE bed
(
    bedId      VARCHAR(2) NOT NULL,
    hospitalId uuid       NOT NULL,
    status     statusType NOT NULL,
    FOREIGN KEY (hospitalId) REFERENCES hospital (hospitalId),
    PRIMARY KEY (bedId, hospitalId)
);

/*CREATE Table queue(
    queueId INT NOT NULL,
    status statusType NOT NULL,
    PRIMARY KEY (queueId)
);*/
/*CREATE Table queue(
                      queueId serial NOT NULL unique,
                      created_timestamp timestamp not null
                          default current_timestamp,
                      PRIMARY KEY (queueId)
);
*/
/*CREATE Table queue(
                      queueId serial NOT NULL unique,
                      created_timestamp timestamp not null
                          default current_timestamp,
                      PRIMARY KEY (queueId)
);
*/
CREATE Table queue
(
    queueId           uuid      NOT NULL,
    created_timestamp timestamp not null
        default current_timestamp,
    PRIMARY KEY (queueId)
);

CREATE TABLE record
(
    patientId      uuid NOT NULL,
    serialNumber   uuid DEFAULT uuid_generate_v4(),
    bedId          VARCHAR(2) NULL,
    hospitalId     uuid NULL,
    RegDate        DATE NOT NULL,
    AdmittedDate   DATE NULL,
    DischargedDate DATE NULL,
    closed         DATE NULL,
    queueId        uuid NULL,
    FOREIGN KEY (patientId) REFERENCES patient (patientId),
    FOREIGN KEY (queueId) REFERENCES queue (queueId),
    FOREIGN KEY (bedId, hospitalId) REFERENCES bed (bedId, hospitalId),
    PRIMARY KEY (serialNumber)
);

CREATE TYPE severityLevel AS ENUM('low', 'moderate',  'critical');

CREATE TABLE severity
(
    severityId   UUID          NOT NULL,
    level        severityLevel NOT NULL,
    doctorId     uuid          NOT NULL,
    markedDate   DATE          NOT NULL,
    serialNumber uuid          NOT NULL,
    FOREIGN KEY (doctorId) REFERENCES person (userId),
    FOREIGN KEY (serialNumber) REFERENCES record (serialNumber),
    PRIMARY KEY (severityId)
);

CREATE VIEW queue_view as
SELECT ROW_NUMBER() OVER(ORDER BY queue.created_timestamp) AS queue_number,queue.queueId,
       queue.created_timestamp,
       patient.patientId,
       patient.district,
       patient.location_x,
       patient.location_y,
       record.serialNumber
FROM queue
         JOIN record ON queue.queueId = record.queueId
         JOIN patient ON record.patientId = patient.patientId;