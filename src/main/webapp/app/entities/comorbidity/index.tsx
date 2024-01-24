import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Comorbidity from './comorbidity';
import ComorbidityDetail from './comorbidity-detail';
import ComorbidityUpdate from './comorbidity-update';
import ComorbidityDeleteDialog from './comorbidity-delete-dialog';

const ComorbidityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Comorbidity />} />
    <Route path="new" element={<ComorbidityUpdate />} />
    <Route path=":id">
      <Route index element={<ComorbidityDetail />} />
      <Route path="edit" element={<ComorbidityUpdate />} />
      <Route path="delete" element={<ComorbidityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ComorbidityRoutes;
