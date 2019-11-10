import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {Routes, RouterModule} from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent }  from './app.component';

import { FormsModule } from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination'
 

import {ReactiveFormsModule} from '@angular/forms';
import {  HTTP_INTERCEPTORS } from '@angular/common/http';
import {FileUploadModule} from 'ng2-file-upload';
import {CommonModule} from '@angular/common';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { Homecomponent } from './_component/app.homecomponent';
import { AdminComponent } from './_component/app.admincomponent';
import { UserComponent } from './_component/app.usercomponent';
import { ApproveUserComponent } from './_component/app.approveusercomponent';
import { RegisterComponent } from './_component/app.registercomponent';
import { BasicAuthHtppInterceptorService } from './_service/app.basicauthinterceptorservice';
import { ApproveNomineeComponent } from './_component/app.approvenomineecomponent';
import { AddPollComponent } from './_component/app.addpollcomponent';
import { RegisterNomineeComponent } from './_component/app.registernomineecomponent';
import { VoteComponent } from './_component/app.votecomponent';
import { Error403Component } from './_component/app.403pagecomponent';
import { Error404Component } from './_component/app.404pagecomponent';


const routes:Routes = [
    { path: '', redirectTo: 'home', pathMatch:'full'},
    { path: 'home', component:Homecomponent },
    { path: 'admin', component:AdminComponent },
    { path: 'user', component:UserComponent },
    { path: 'approveuser', component:ApproveUserComponent },
    { path: 'register', component:RegisterComponent},
    { path: 'approvenominee', component:ApproveNomineeComponent},
    { path:'addpoll', component:AddPollComponent },
    { path:'registernominee', component:RegisterNomineeComponent },
    { path:'vote', component:VoteComponent },
    { path:'error403', component:Error403Component },
    { path:'**', component:Error404Component }
];

@NgModule({
    imports: [
        BrowserModule, FormsModule, HttpClientModule, ReactiveFormsModule, CommonModule, Ng2SmartTableModule, RouterModule.forRoot(routes), FileUploadModule,NgxPaginationModule
    ],
    declarations: [
        AppComponent, Homecomponent, AdminComponent, UserComponent, ApproveUserComponent, RegisterComponent, ApproveNomineeComponent,
        AddPollComponent, RegisterNomineeComponent, VoteComponent, Error403Component, Error404Component
		], 

    providers: [{provide:HTTP_INTERCEPTORS, useClass:BasicAuthHtppInterceptorService, multi:true}],

    bootstrap: [AppComponent]
})

export class AppModule { }