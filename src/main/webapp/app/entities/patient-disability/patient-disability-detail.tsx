import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient-disability.reducer';

export const PatientDisabilityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientDisabilityEntity = useAppSelector(state => state.patientDisability.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientDisabilityDetailsHeading">Patient Disability</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{patientDisabilityEntity.id}</dd>
          <dt>Patient</dt>
          <dd>{patientDisabilityEntity.patient ? patientDisabilityEntity.patient.email : ''}</dd>
          <dt>Disability</dt>
          <dd>{patientDisabilityEntity.disability ? patientDisabilityEntity.disability.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient-disability" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient-disability/${patientDisabilityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientDisabilityDetail;
