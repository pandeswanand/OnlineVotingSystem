import { Component } from "@angular/core";
import { User } from "../_model/app.user";
import { UserService } from "../_service/app.userservice";

@Component({
    selector:'approveuser',
    templateUrl:'../_html/app.approveuser.html'
})
export class ApproveUserComponent{
    users:User[]=[];

    constructor(private service:UserService){}

    getUserList(){
        this.service.getUsers().subscribe((data:User[])=>this.users=data);
    }

    approveUser(id:number){
        this.service.approveUser(id).subscribe((data)=>{alert(data)}, error=>{alert(error.error)});
    }
}