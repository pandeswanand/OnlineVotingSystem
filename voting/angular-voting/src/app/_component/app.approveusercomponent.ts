import { Component, OnInit } from "@angular/core";
import { User } from "../_model/app.user";
import { UserService } from "../_service/app.userservice";

@Component({
    selector:'approveuser',
    templateUrl:'../_html/app.approveuser.html'
})
export class ApproveUserComponent implements OnInit{
    users:User[]=[];

    settings = {
        columns: {
          userId: {
            title: 'User Id'
          },
          username: {
            title: 'Username'
          },
          aadharNo: {
            title: 'Aadhar No'
          },
          age: {
            title: 'Age'
          },
          pollLocation: {
            title: 'Poll Locataion'
          }
        },
        actions: {
            delete: false,
            add: false,
            edit: false,
            custom: [{ name: 'custom', title: '<i class="nb-compose">Approve</i>' }],
            position: 'right'
        },
        pager:{
            display:true,
            perPage: 5
        },
        attr: {
            class: 'fonts'
        }
      };

    constructor(private service:UserService){}

    ngOnInit(){
        this.service.getUsers().subscribe((data:User[])=>{this.users=data; console.log(this.users)});
    }

    approveUser(event){
        this.service.approveUser(event.data.userId).subscribe((data)=>{alert(data)}, error=>{alert(error.error)});
        window.location.reload();
    }
}