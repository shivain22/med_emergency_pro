export interface IPatient {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  email?: string;
  gender?: string;
  age?: number;
  mobileNumber?: string;
}

export const defaultValue: Readonly<IPatient> = {};
