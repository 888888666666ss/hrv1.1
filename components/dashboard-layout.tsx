"use client"

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Card } from "@/components/ui/card"
import {
  Search,
  Bell,
  MessageSquare,
  Upload,
  Settings,
  HelpCircle,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  LayoutGrid,
  Home,
  BarChart3,
  Users,
  FileText,
  UserCircle,
  Calendar,
} from "lucide-react"

export function DashboardLayout() {
  return (
    <div className="flex h-screen bg-gray-50">
      {/* Left Sidebar */}
      <aside className="w-16 bg-white border-r border-gray-200 flex flex-col items-center py-4 gap-6">
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <LayoutGrid className="w-5 h-5 text-gray-600" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <Home className="w-5 h-5 text-blue-500" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <BarChart3 className="w-5 h-5 text-gray-400" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <Users className="w-5 h-5 text-gray-400" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <FileText className="w-5 h-5 text-gray-400" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <UserCircle className="w-5 h-5 text-gray-400" />
        </button>
        <button className="p-2 hover:bg-gray-100 rounded-lg">
          <Calendar className="w-5 h-5 text-gray-400" />
        </button>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <header className="bg-white border-b border-gray-200 px-6 py-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-6">
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 rounded-full bg-red-500" />
                <div className="w-2 h-2 rounded-full bg-yellow-500" />
                <div className="w-2 h-2 rounded-full bg-green-500" />
              </div>
              <h1 className="text-lg font-medium text-gray-900">招聘管理工作台</h1>
            </div>
          </div>
        </header>

        {/* Sub Header */}
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-6">
              <div className="flex items-center gap-2">
                <Home className="w-5 h-5 text-teal-500" />
                <span className="font-medium text-gray-900">Baiao</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-600">
                <Settings className="w-4 h-4" />
                <span>全局视野工作台</span>
                <ChevronDown className="w-4 h-4" />
              </div>
            </div>

            <div className="flex items-center gap-4">
              <button className="p-2 hover:bg-gray-100 rounded-lg relative">
                <Search className="w-5 h-5 text-gray-600" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg relative">
                <Bell className="w-5 h-5 text-gray-600" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg relative">
                <MessageSquare className="w-5 h-5 text-gray-600" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg">
                <Upload className="w-5 h-5 text-gray-600" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg">
                <Settings className="w-5 h-5 text-gray-600" />
              </button>
              <button className="p-2 hover:bg-gray-100 rounded-lg">
                <HelpCircle className="w-5 h-5 text-gray-600" />
              </button>
              <div className="flex items-center gap-2">
                <Avatar className="w-8 h-8">
                  <AvatarImage src="/placeholder.svg?height=32&width=32" />
                  <AvatarFallback>张</AvatarFallback>
                </Avatar>
                <span className="text-sm text-gray-700">张经理</span>
                <ChevronDown className="w-4 h-4 text-gray-600" />
              </div>
            </div>
          </div>
        </div>

        {/* User Greeting Section */}
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Avatar className="w-12 h-12">
                <AvatarImage src="/placeholder.svg?height=48&width=48" />
                <AvatarFallback>张</AvatarFallback>
              </Avatar>
              <div>
                <h2 className="text-lg font-medium text-gray-900">张经理, 下午好</h2>
                <p className="text-sm text-gray-500">人力资源部 · 招聘经理 · 北京市朝阳区</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <div className="relative flex-1 max-w-md">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <Input placeholder="搜索候选人姓名、职位等信息" className="pl-10 pr-4" />
              </div>
              <Button className="bg-blue-500 hover:bg-blue-600 text-white">搜索</Button>
              <Button variant="outline">新建面试</Button>
            </div>
          </div>

          {/* Tabs */}
          <div className="flex items-center gap-6 mt-4">
            <button className="px-4 py-2 text-sm font-medium text-blue-500 border-b-2 border-blue-500">任务</button>
            <button className="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-900">流程</button>
            <button className="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-900">其它</button>
          </div>
        </div>

        {/* Main Dashboard Content */}
        <div className="flex-1 overflow-auto">
          <div className="p-6">
            <div className="flex gap-6">
              {/* Left Content */}
              <div className="flex-1">
                {/* Stats Cards */}
                <div className="grid grid-cols-5 gap-4 mb-6">
                  <Card className="p-4 border-l-4 border-orange-500">
                    <div className="text-xs text-gray-500 mb-1">简历筛选</div>
                    <div className="text-2xl font-semibold text-gray-900">870</div>
                    <div className="text-xs text-gray-400 mt-1">待处理简历 45</div>
                  </Card>

                  <Card className="p-4 border-l-4 border-blue-500">
                    <div className="text-xs text-gray-500 mb-1">初试</div>
                    <div className="text-2xl font-semibold text-gray-900">20</div>
                    <div className="text-xs text-gray-400 mt-1">本周已安排 12</div>
                  </Card>

                  <Card className="p-4 border-l-4 border-teal-500">
                    <div className="text-xs text-gray-500 mb-1">复试</div>
                    <div className="text-2xl font-semibold text-gray-900">36</div>
                    <div className="text-xs text-gray-400 mt-1">本周已安排 8</div>
                  </Card>

                  <Card className="p-4 border-l-4 border-purple-500">
                    <div className="text-xs text-gray-500 mb-1">Offer</div>
                    <div className="text-2xl font-semibold text-gray-900">8</div>
                    <div className="text-xs text-gray-400 mt-1">待发放 3</div>
                  </Card>

                  <Card className="p-4 border-l-4 border-green-500">
                    <div className="text-xs text-gray-500 mb-1">入职</div>
                    <div className="text-2xl font-semibold text-gray-900">5</div>
                    <div className="text-xs text-gray-400 mt-1">本月入职 5</div>
                  </Card>
                </div>

                {/* Schedule Section */}
                <Card className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center gap-2">
                      <div className="w-1 h-5 bg-orange-500 rounded" />
                      <h3 className="font-medium text-gray-900">我的面试安排</h3>
                      <Badge variant="secondary" className="text-xs">
                        更新时间
                      </Badge>
                    </div>

                    <div className="flex items-center gap-4">
                      <button className="text-sm text-gray-600 hover:text-gray-900">回到今天</button>
                      <button className="text-sm text-gray-600 hover:text-gray-900">快速查看</button>
                      <Button size="sm" className="bg-blue-500 hover:bg-blue-600 text-white">
                        创建面试
                      </Button>
                      <button className="text-sm text-gray-600 hover:text-gray-900">数据同步设置</button>
                      <ChevronDown className="w-4 h-4 text-gray-600" />
                    </div>
                  </div>

                  {/* Calendar Week View */}
                  <div className="flex items-center gap-2 mb-6">
                    <button className="p-1 hover:bg-gray-100 rounded">
                      <ChevronLeft className="w-4 h-4 text-gray-600" />
                    </button>

                    <div className="flex-1 grid grid-cols-7 gap-2">
                      {[
                        { day: "周一", date: "26" },
                        { day: "周二", date: "27" },
                        { day: "周三", date: "28" },
                        { day: "周四", date: "29", active: true },
                        { day: "周五", date: "30" },
                        { day: "周六", date: "1" },
                        { day: "周日", date: "2" },
                      ].map((item, index) => (
                        <div
                          key={index}
                          className={`text-center p-2 rounded-lg ${
                            item.active ? "bg-blue-500 text-white" : "hover:bg-gray-100"
                          }`}
                        >
                          <div className="text-xs mb-1">{item.day}</div>
                          <div className="text-lg font-medium">{item.date}</div>
                        </div>
                      ))}
                    </div>

                    <button className="p-1 hover:bg-gray-100 rounded">
                      <ChevronRight className="w-4 h-4 text-gray-600" />
                    </button>
                  </div>

                  {/* Schedule Items */}
                  <div className="space-y-4">
                    <div className="flex gap-4 p-4 hover:bg-gray-50 rounded-lg">
                      <div className="text-sm text-gray-600 w-24">09:00 - 10:00</div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Badge variant="secondary" className="bg-teal-50 text-teal-600">
                            已完成
                          </Badge>
                          <span className="font-medium text-gray-900">高级产品经理 - 王安</span>
                        </div>
                        <div className="text-sm text-gray-500">
                          电话面试 · 视频 · 面试 北京市朝阳区北京朝阳区朝阳大悦城北京朝阳区朝阳大悦城
                        </div>
                      </div>
                      <Badge className="bg-green-50 text-green-600 border-green-200">面试</Badge>
                    </div>

                    <div className="flex gap-4 p-4 hover:bg-gray-50 rounded-lg">
                      <div className="text-sm text-gray-600 w-24">14:00 - 16:00</div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Badge variant="secondary" className="bg-teal-50 text-teal-600">
                            已完成
                          </Badge>
                          <span className="font-medium text-gray-900">2023级校产品经理 - 集体面试入职</span>
                        </div>
                        <div className="text-sm text-gray-500">
                          视频 · 面试 北京市朝阳区北京朝阳区朝阳大悦城北京朝阳区朝阳大悦城
                        </div>
                      </div>
                      <span className="text-sm text-blue-500">2 通过</span>
                    </div>

                    <div className="flex gap-4 p-4 hover:bg-gray-50 rounded-lg">
                      <div className="text-sm text-gray-600 w-24">16:30 - 19:00</div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Badge variant="secondary" className="bg-teal-50 text-teal-600">
                            已完成
                          </Badge>
                          <span className="font-medium text-gray-900">高级产品经理 - 初级</span>
                        </div>
                        <div className="text-sm text-gray-500">
                          视频 · 面试 北京市朝阳区北京朝阳区朝阳大悦城北京朝阳区朝阳大悦城{" "}
                          <span className="text-blue-500">视频面试地址 &gt;</span>
                        </div>
                      </div>
                      <button className="text-sm text-blue-500">取消预约</button>
                    </div>

                    <div className="flex gap-4 p-4 hover:bg-gray-50 rounded-lg">
                      <div className="text-sm text-gray-600 w-24">20:00 - 20:30</div>
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Badge variant="secondary" className="bg-teal-50 text-teal-600">
                            已完成
                          </Badge>
                          <span className="font-medium text-gray-900">高级产品经理 - 王安生</span>
                        </div>
                        <div className="text-sm text-gray-500">
                          视频 · 面试 北京市朝阳区北京朝阳区朝阳大悦城北京朝阳区朝阳大悦城
                        </div>
                      </div>
                      <button className="text-sm text-blue-500">取消预约</button>
                    </div>
                  </div>
                </Card>
              </div>

              {/* Right Sidebar */}
              <div className="w-80 space-y-4">
                <Card className="p-4">
                  <div className="flex items-center justify-between mb-4">
                    <span className="text-2xl font-semibold text-gray-900">0</span>
                    <span className="text-sm text-gray-600">待跟进的候选人</span>
                  </div>
                  <div className="text-xs text-gray-400">已完成跟进 0</div>
                </Card>

                <Card className="p-4">
                  <div className="flex items-center justify-between mb-4">
                    <span className="text-2xl font-semibold text-gray-900">42</span>
                    <span className="text-sm text-gray-600">面试通过率</span>
                  </div>
                  <div className="text-xs text-gray-400">本月数据统计</div>
                </Card>

                <Card className="p-4">
                  <div className="flex items-center justify-between mb-4">
                    <span className="text-2xl font-semibold text-gray-900">5</span>
                    <span className="text-sm text-gray-600">待反馈的面试</span>
                  </div>
                  <div className="text-xs text-gray-400">需要填写评价 5</div>
                </Card>

                <Card className="p-4">
                  <div className="flex items-center justify-between mb-4">
                    <span className="text-2xl font-semibold text-gray-900">5</span>
                    <span className="text-sm text-gray-600">待确认的入职</span>
                  </div>
                  <div className="text-xs text-gray-400">本月待入职 5</div>
                </Card>

                <Card className="p-6 bg-gradient-to-br from-orange-50 to-blue-50">
                  <div className="flex items-center gap-2 mb-4">
                    <span className="text-2xl font-semibold text-orange-500">2</span>
                    <span className="text-sm text-gray-700">待发放Offer</span>
                  </div>
                  <div className="text-xs text-gray-500 mb-4">查看详情 &gt;</div>
                  <div className="flex justify-center">
                    <img src="/person-with-lightbulb-illustration.jpg" alt="Illustration" className="w-32 h-32" />
                  </div>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
