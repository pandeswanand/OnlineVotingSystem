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

    getNominees(){
        return this.http.get("http://localhost:9088/admin/list/nominee/unapproved");
    }

    approveNominee(id:number, center:string){
        return this.http.post("http://localhost:9088/admin/nominee/approve?userid="+id+"&center="+center,null);
    }

    registerNominee(id:any, area:string){
        return this.http.post("http://localhost:9088/admin/nominee/add?userid="+id+"&area="+area, null);
    }

    searchUserPoll(email:any){
        return this.http.get("http://localhost:9088/admin/search/poll?email="+email);
    }

    getCenterNominees(center:string){
        return this.http.get("http://localhost:9088/admin/list/nominee?center="+center);
    }

    vote(userid:any, id:any){
        return this.http.post("http://localhost:9088/admin/vote?userid="+userid+"&id="+id,null);
    }
}