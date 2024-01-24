import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IPatientVital } from 'app/shared/model/patient-vital.model';
import { getEntity, updateEntity, createEntity, reset } from './patient-vital.reducer';

export const PatientVitalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const patientVitalEntity = useAppSelector(state => state.patientVital.entity);
  const loading = useAppSelector(state => state.patientVital.loading);
  const updating = useAppSelector(state => state.patientVital.updating);
  const updateSuccess = useAppSelector(state => state.patientVital.updateSuccess);

  const handleClose = () => {
    navigate('/patient-vital' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timeOfMeasurement = convertDateTimeToServer(values.timeOfMeasurement);

    const entity = {
      ...patientVitalEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timeOfMeasurement: displayDefaultDateTime(),
        }
      : {
          ...patientVitalEntity,
          timeOfMeasurement: convertDateTimeFromServer(patientVitalEntity.timeOfMeasurement),
          patient: patientVitalEntity?.patient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="auriHealthCareApp.patientVital.home.createOrEditLabel" data-cy="PatientVitalCreateUpdateHeading">
            Create or edit a Patient Vital
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="patient-vital-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Pulse Rate" id="patient-vital-pulseRate" name="pulseRate" data-cy="pulseRate" type="text" />
              <ValidatedField
                label="Blood Pressure"
                id="patient-vital-bloodPressure"
                name="bloodPressure"
                data-cy="bloodPressure"
                type="text"
              />
              <ValidatedField label="Respiration" id="patient-vital-respiration" name="respiration" data-cy="respiration" type="text" />
              <ValidatedField label="Spo 2" id="patient-vital-spo2" name="spo2" data-cy="spo2" type="text" />
              <ValidatedField
                label="Time Of Measurement"
                id="patient-vital-timeOfMeasurement"
                name="timeOfMeasurement"
                data-cy="timeOfMeasurement"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="patient-vital-patient" name="patient" data-cy="patient" label="Patient" type="select" required>
                <option value="" key="0" />
                {patients
                  ? patients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.email}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient-vital" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PatientVitalUpdate;
