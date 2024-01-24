import { IPatient } from 'app/shared/model/patient.model';

export interface IComment {
  id?: number;
  comment?: string | null;
  patient?: IPatient;
}

export const defaultValue: Readonly<IComment> = {};
