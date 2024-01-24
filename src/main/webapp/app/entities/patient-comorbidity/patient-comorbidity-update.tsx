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
import { IComorbidity } from 'app/shared/model/comorbidity.model';
import { getEntities as getComorbidities } from 'app/entities/comorbidity/comorbidity.reducer';
import { IPatientComorbidity } from 'app/shared/model/patient-comorbidity.model';
import { getEntity, updateEntity, createEntity, reset } from './patient-comorbidity.reducer';

export const PatientComorbidityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const comorbidities = useAppSelector(state => state.comorbidity.entities);
  const patientComorbidityEntity = useAppSelector(state => state.patientComorbidity.entity);
  const loading = useAppSelector(state => state.patientComorbidity.loading);
  const updating = useAppSelector(state => state.patientComorbidity.updating);
  const updateSuccess = useAppSelector(state => state.patientComorbidity.updateSuccess);

  const handleClose = () => {
    navigate('/patient-comorbidity' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
    dispatch(getComorbidities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...patientComorbidityEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
      comorbidity: comorbidities.find(it => it.id.toString() === values.comorbidity.toString()),
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
          ...patientComorbidityEntity,
          patient: patientComorbidityEntity?.patient?.id,
          comorbidity: patientComorbidityEntity?.comorbidity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="auriHealthCareApp.patientComorbidity.home.createOrEditLabel" data-cy="PatientComorbidityCreateUpdateHeading">
            Create or edit a Patient Comorbidity
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
                <ValidatedField name="id" required readOnly id="patient-comorbidity-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField id="patient-comorbidity-patient" name="patient" data-cy="patient" label="Patient" type="select" required>
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
                id="patient-comorbidity-comorbidity"
                name="comorbidity"
                data-cy="comorbidity"
                label="Comorbidity"
                type="select"
                required
              >
                <option value="" key="0" />
                {comorbidities
                  ? comorbidities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient-comorbidity" replace color="info">
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

export default PatientComorbidityUpdate;
