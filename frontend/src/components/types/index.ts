export interface User {
  username: number | undefined;
  role: string | undefined;
  token: string | undefined;
}

export enum Role {
  CLIENT="CLIENT",
  OWNER="OWNER",
  MANAGER="MANAGER"
}

export enum Status {
  UNDER_CONSIDERATION="UNDER_CONSIDERATION",
  BOOKED="BOOKED",
  OCCUPIED="OCCUPIED",
  AWAITING_REVIEW="AWAITING_REVIEW",
  CLOSED="CLOSED",
  REJECTED="REJECTED"
}