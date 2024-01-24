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
import { IDisability } from 'app/shared/model/disability.model';
import { getEntities as getDisabilities } from 'app/entities/disability/disability.reducer';
import { IPatientDisability } from 'app/shared/model/patient-disability.model';
import { getEntity, updateEntity, createEntity, reset } from './patient-disability.reducer';

export const PatientDisabilityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const disabilities = useAppSelector(state => state.disability.entities);
  const patientDisabilityEntity = useAppSelector(state => state.patientDisability.entity);
  const loading = useAppSelector(state => state.patientDisability.loading);
  const updating = useAppSelector(state => state.patientDisability.updating);
  const updateSuccess = useAppSelector(state => state.patientDisability.updateSuccess);

  const handleClose = () => {
    navigate('/patient-disability' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
    dispatch(getDisabilities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...patientDisabilityEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
      disability: disabilities.find(it => it.id.toString() === values.disability.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...patientDisabilityEntity,
          patient: patientDisabilityEntity?.patient?.id,
          disability: patientDisabilityEntity?.disability?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="auriHealthCareApp.patientDisability.home.createOrEditLabel" data-cy="PatientDisabilityCreateUpdateHeading">
            Create or edit a Patient Disability
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
                <ValidatedField name="id" required readOnly id="patient-disability-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField id="patient-disability-patient" name="patient" data-cy="patient" label="Patient" type="select" required>
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
              <ValidatedField
                id="patient-disability-disability"
                name="disability"
                data-cy="disability"
                label="Disability"
                type="select"
                required
              >
                <option value="" key="0" />
                {disabilities
                  ? disabilities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient-disability" replace color="info">
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

export default PatientDisabilityUpdate;
