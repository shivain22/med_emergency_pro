import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PatientDisability from './patient-disability';
import PatientDisabilityDetail from './patient-disability-detail';
import PatientDisabilityUpdate from './patient-disability-update';
import PatientDisabilityDeleteDialog from './patient-disability-delete-dialog';

const PatientDisabilityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PatientDisability />} />
    <Route path="new" element={<PatientDisabilityUpdate />} />
    <Route path=":id">
      <Route index element={<PatientDisabilityDetail />} />
      <Route path="edit" element={<PatientDisabilityUpdate />} />
      <Route path="delete" element={<PatientDisabilityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PatientDisabilityRoutes;
