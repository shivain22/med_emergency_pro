import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient-vital.reducer';

export const PatientVitalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientVitalEntity = useAppSelector(state => state.patientVital.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientVitalDetailsHeading">Patient Vital</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{patientVitalEntity.id}</dd>
          <dt>
            <span id="pulseRate">Pulse Rate</span>
          </dt>
          <dd>{patientVitalEntity.pulseRate}</dd>
          <dt>
            <span id="bloodPressure">Blood Pressure</span>
          </dt>
          <dd>{patientVitalEntity.bloodPressure}</dd>
          <dt>
            <span id="respiration">Respiration</span>
          </dt>
          <dd>{patientVitalEntity.respiration}</dd>
          <dt>
            <span id="spo2">Spo 2</span>
          </dt>
          <dd>{patientVitalEntity.spo2}</dd>
          <dt>
            <span id="timeOfMeasurement">Time Of Measurement</span>
          </dt>
          <dd>
            {patientVitalEntity.timeOfMeasurement ? (
              <TextFormat value={patientVitalEntity.timeOfMeasurement} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Patient</dt>
          <dd>{patientVitalEntity.patient ? patientVitalEntity.patient.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient-vital" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient-vital/${patientVitalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientVitalDetail;
