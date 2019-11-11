import { Component, OnInit } from "@angular/core";
import { User } from "../_model/app.user";
import { UserService } from "../_service/app.userservice";
import { Router } from "@angular/router";

@Component({
    selector:'approvenominee',
    templateUrl:'../_html/app.approvenominee.html'
})
export class ApproveNomineeComponent implements OnInit{
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
          contestFrom: {
            title: 'Contest Location'
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

    constructor(private service:UserService, private router:Router){}

    ngOnInit(){
      if(sessionStorage.getItem("role")!= "admin"){
          this.router.navigate(['/error403'])
      }
      this.service.getNominees().subscribe((data:User[])=>{this.users=data; console.log(this.users)});
    }

    approveNominee(event){
        this.service.approveNominee(event.data.userId, event.data.contestFrom).subscribe((data)=>{alert(data)}, error=>{alert(error.error)});
        window.location.reload();
    }
}
