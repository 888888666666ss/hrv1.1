import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Gift, Info, ChevronLeft } from "lucide-react"

export default function ReferralPage() {
  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="mx-auto max-w-4xl">
        {/* Header */}
        <div className="mb-6 flex items-center gap-4">
          <Button variant="ghost" size="icon">
            <ChevronLeft className="h-5 w-5" />
          </Button>
          <h1 className="text-2xl font-semibold">花式内推</h1>
        </div>

        <div className="grid gap-6 lg:grid-cols-2">
          {/* Left Column */}
          <div className="space-y-6">
            {/* Profile Card */}
            <Card>
              <CardHeader>
                <Tabs defaultValue="referral" className="w-full">
                  <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="referral">推荐记录</TabsTrigger>
                    <TabsTrigger value="bonus">奖金记录</TabsTrigger>
                  </TabsList>
                </Tabs>
              </CardHeader>
              <CardContent>
                <div className="flex items-center gap-4">
                  <div className="relative">
                    <Avatar className="h-16 w-16">
                      <AvatarImage src="/professional-asian-person-headshot.jpg" />
                      <AvatarFallback>内推</AvatarFallback>
                    </Avatar>
                    <Badge className="absolute -bottom-1 left-1/2 -translate-x-1/2 bg-blue-500 text-xs">内推官</Badge>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Referral Form Card */}
            <Card>
              <CardHeader>
                <div className="flex items-center gap-2">
                  <CardTitle className="text-base font-medium text-teal-600">候选人配置推荐连通接</CardTitle>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <Tabs defaultValue="referral" className="w-full">
                  <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="referral">推荐记录</TabsTrigger>
                    <TabsTrigger value="bonus">奖金记录</TabsTrigger>
                    <TabsTrigger value="points">积分记录</TabsTrigger>
                  </TabsList>
                </Tabs>

                <div className="flex items-start gap-2">
                  <Info className="mt-1 h-4 w-4 text-blue-500 flex-shrink-0" />
                  <div className="grid gap-3 flex-1">
                    <Input placeholder="候选人姓名" />
                    <Input placeholder="候选人手机号" />
                    <Input placeholder="候选人邮箱" />
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Right Column */}
          <div className="space-y-6">
            {/* User Stats Card */}
            <Card>
              <CardContent className="pt-6">
                <div className="flex items-start gap-4">
                  <Avatar className="h-12 w-12">
                    <AvatarImage src="/professional-asian-person-headshot.jpg" />
                    <AvatarFallback>小北</AvatarFallback>
                  </Avatar>
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-3">
                      <span className="font-medium">小北</span>
                      <Badge className="bg-blue-500">LV.5</Badge>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <div className="text-xs text-gray-500 mb-1">已获奖金</div>
                        <div className="text-lg font-semibold text-teal-600">999,999</div>
                      </div>
                      <div>
                        <div className="text-xs text-gray-500 mb-1">已获积分</div>
                        <div className="text-lg font-semibold text-teal-600">200</div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="mt-4 pt-4 border-t">
                  <div className="text-sm text-gray-600 mb-1">被你推荐该项目</div>
                  <div className="font-medium">HJKIY6</div>
                </div>

                <div className="mt-4 pt-4 border-t">
                  <div className="flex items-center gap-2 text-orange-500">
                    <Gift className="h-5 w-5" />
                    <span className="font-medium">奖福利商城</span>
                  </div>
                </div>

                <Button className="w-full mt-4 bg-gray-100 text-gray-700 hover:bg-gray-200">
                  <span className="mr-2">👤</span>
                  推荐记录
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
