import { IPatient } from 'app/shared/model/patient.model';
import { IDisability } from 'app/shared/model/disability.model';

export interface IPatientDisability {
  id?: number;
  patient?: IPatient;
  disability?: IDisability;
}

export const defaultValue: Readonly<IPatientDisability> = {};
