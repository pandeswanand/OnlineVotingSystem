import { Address } from "./app.address";
import { Poll } from "./app.poll";

export class User{
    userId:number;
    username:string;
    password:string;
    emailId:string;
    aadharNo:number;
    pollLocation:string;
    age:number;
    address:Address;
    isAdmin:boolean;
    poll:Poll;
}