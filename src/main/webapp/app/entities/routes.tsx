import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Patient from './patient';
import Disability from './disability';
import Comorbidity from './comorbidity';
import PatientVital from './patient-vital';
import Comment from './comment';
import PatientComorbidity from './patient-comorbidity';
import PatientDisability from './patient-disability';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="patient/*" element={<Patient />} />
        <Route path="disability/*" element={<Disability />} />
        <Route path="comorbidity/*" element={<Comorbidity />} />
        <Route path="patient-vital/*" element={<PatientVital />} />
        <Route path="comment/*" element={<Comment />} />
        <Route path="patient-comorbidity/*" element={<PatientComorbidity />} />
        <Route path="patient-disability/*" element={<PatientDisability />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
