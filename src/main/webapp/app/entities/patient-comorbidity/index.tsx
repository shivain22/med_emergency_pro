import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PatientComorbidity from './patient-comorbidity';
import PatientComorbidityDetail from './patient-comorbidity-detail';
import PatientComorbidityUpdate from './patient-comorbidity-update';
import PatientComorbidityDeleteDialog from './patient-comorbidity-delete-dialog';

const PatientComorbidityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PatientComorbidity />} />
    <Route path="new" element={<PatientComorbidityUpdate />} />
    <Route path=":id">
      <Route index element={<PatientComorbidityDetail />} />
      <Route path="edit" element={<PatientComorbidityUpdate />} />
      <Route path="delete" element={<PatientComorbidityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PatientComorbidityRoutes;
