import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Disability from './disability';
import DisabilityDetail from './disability-detail';
import DisabilityUpdate from './disability-update';
import DisabilityDeleteDialog from './disability-delete-dialog';

const DisabilityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Disability />} />
    <Route path="new" element={<DisabilityUpdate />} />
    <Route path=":id">
      <Route index element={<DisabilityDetail />} />
      <Route path="edit" element={<DisabilityUpdate />} />
      <Route path="delete" element={<DisabilityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DisabilityRoutes;
