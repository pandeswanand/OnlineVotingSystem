import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

// const httpOptions = {
//     headers: new HttpHeaders({
//       'Content-Type':  undefined
//     })
//   };

@Injectable({
    providedIn:'root'
})
export class UserService{

    constructor(private http:HttpClient){}

    getUsers(){
        return this.http.get("http://localhost:9088/list/user/unapproved");
    }

    approveUser(id:number){
        return this.http.post("http://localhost:9088/user/approve?userid="+id,null);
    }

    getNominees(){
        return this.http.get("http://localhost:9088/list/nominee/unapproved");
    }

    approveNominee(id:number, center:string){
        return this.http.post("http://localhost:9088/nominee/approve?userid="+id+"&center="+center,null);
    }

    registerNominee(id:any, area:string){
        return this.http.post("http://localhost:9088/nominee/add?userid="+id+"&area="+area, null);
    }

    searchUserPoll(email:any){
        return this.http.get("http://localhost:9088/search/poll?email="+email);
    }

    getCenterNominees(center:string){
        return this.http.get("http://localhost:9088/list/nominee?center="+center);
    }

    vote(userid:any, id:any){
        return this.http.post("http://localhost:9088/vote?userid="+userid+"&id="+id,null);
    }

    uploadAadhar(file:any){
        console.log(file);
        let formData = new FormData();
        formData.append("aadhar", file);
        return this.http.post("http://localhost:9088/upload/aadhar",formData);
    }
}