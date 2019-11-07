import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn:'root'
})
export class UserService{

    constructor(private http:HttpClient){}

    getUsers(){
        return this.http.get("http://localhost:9088/admin/list/user/unapproved");
    }

    approveUser(id:number){
        return this.http.post("http://localhost:9088/admin/user/approve?userid="+id,null);
    }
}