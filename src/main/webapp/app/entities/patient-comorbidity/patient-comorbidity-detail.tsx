import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient-comorbidity.reducer';

export const PatientComorbidityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientComorbidityEntity = useAppSelector(state => state.patientComorbidity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientComorbidityDetailsHeading">Patient Comorbidity</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{patientComorbidityEntity.id}</dd>
          <dt>Patient</dt>
          <dd>{patientComorbidityEntity.patient ? patientComorbidityEntity.patient.email : ''}</dd>
          <dt>Comorbidity</dt>
          <dd>{patientComorbidityEntity.comorbidity ? patientComorbidityEntity.comorbidity.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient-comorbidity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient-comorbidity/${patientComorbidityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientComorbidityDetail;
