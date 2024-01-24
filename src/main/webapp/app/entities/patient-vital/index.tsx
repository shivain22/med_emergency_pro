import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PatientVital from './patient-vital';
import PatientVitalDetail from './patient-vital-detail';
import PatientVitalUpdate from './patient-vital-update';
import PatientVitalDeleteDialog from './patient-vital-delete-dialog';

const PatientVitalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PatientVital />} />
    <Route path="new" element={<PatientVitalUpdate />} />
    <Route path=":id">
      <Route index element={<PatientVitalDetail />} />
      <Route path="edit" element={<PatientVitalUpdate />} />
      <Route path="delete" element={<PatientVitalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PatientVitalRoutes;
